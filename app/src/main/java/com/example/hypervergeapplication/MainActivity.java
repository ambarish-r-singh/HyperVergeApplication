package com.example.hypervergeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import co.hyperverge.hypersnapsdk.HyperSnapSDK;
import co.hyperverge.hypersnapsdk.activities.HVDocsActivity;
import co.hyperverge.hypersnapsdk.listeners.APICompletionCallback;
import co.hyperverge.hypersnapsdk.listeners.DocCaptureCompletionHandler;
import co.hyperverge.hypersnapsdk.network.HVNetworkHelper;
import co.hyperverge.hypersnapsdk.objects.HVDocConfig;
import co.hyperverge.hypersnapsdk.objects.HVError;
import co.hyperverge.hypersnapsdk.objects.HyperSnapParams;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize HyperSnap Sdk
        initializeHyperSnapSdk();

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HVDocConfig hvDocConfig = new HVDocConfig();
                HVDocConfig.Document document = HVDocConfig.Document.OTHER;
                document.setAspectRatio(0.625f); // This aspect ratio can be modified based on the document (check next page)
                hvDocConfig.setDocumentType(document);
                hvDocConfig.setShouldShowReviewScreen(true);
                hvDocConfig.setPadding(0.05f);

                System.out.println("************Launch Document capture****************** ");
                HVDocsActivity.start(MainActivity.this,
                        hvDocConfig,
                        MainActivity.this.handleDocCaptureCompletion());
            }
        });
    }

    protected DocCaptureCompletionHandler handleDocCaptureCompletion(){
        return new DocCaptureCompletionHandler() {
            @Override
            public void onResult(HVError error, JSONObject result) {
                if (error != null) {/*Handle error*/
                    // Find Error Codes here
                    System.out.println("ERROR in HyperSnap SDK call " + error.getErrorCode() + "\n" + error.getErrorMessage());
                    System.exit(1);
                } else { /*Handle result*/
                    //The “imageUri” key in the result is the file path of the captured image
                    System.out.println("Successful HyperSnap SDK call ");
                    String val = "";
                    try {
                        val = result.getString("imageUri");
                        System.out.print("imageURI = " + val);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Form request to invoke HyperVerge OCR API
                    JSONObject params = new JSONObject();
                    JSONObject headers = new JSONObject();
                    try {
                        headers.put("transactionId", UUID.randomUUID());
                        headers.put("referenceId", UUID.randomUUID());
                        headers.put("uuid", UUID.randomUUID());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // TODO: remove hardcoded value
                    final String imageUri = "/data/user/0/com.example.hypervergeapplication/files/hv/1601658901746.jpg";
                    // final String imageUri = val; //This is the result from document capture if the error is non null.

                    System.out.println("Invoke HyperVerge OCR API ");
                    HVNetworkHelper.makeOCRCall(MainActivity.this, Constants.HYPERVERGE_KYC_ENDPOINT, imageUri, params, headers, handleHVOcrCallCompletion());
                }
            }
        };

    }

    protected APICompletionCallback handleHVOcrCallCompletion(){
        final APICompletionCallback completionCallback = new APICompletionCallback() {
            @Override
            public void onResult(HVError error, JSONObject result, JSONObject headers) {
                if (error != null) {/*Handle error*/
                    //Find Error Codes here
                    System.out.println("ERROR HyperVerge Read Kyc OCR call error  " + error.getErrorCode() + "\n" + error.getErrorMessage());
                } else {/*Handle result*/
                    //The result is a dictionary of all the extracted fields
                    System.out.println(" Successful HyperVerge read kyc ocr api call  " + result);
                    Intent myIntent = new Intent(getBaseContext(), SecondActivity.class);
                    myIntent.putExtra("response", result.toString());
                    startActivity(myIntent);
                }
            }

        };
        return completionCallback;
    }

    protected void initializeHyperSnapSdk() {
        HyperSnapSDK.setShouldLogOnlyErrors(true);
        HyperSnapSDK.init(getApplicationContext(), Constants.HYPERSNAP_SDK_API_ID, Constants.HYPERSNAP_SDK_API_KEY, HyperSnapParams.Region.India);
        HyperSnapSDK.setShouldEnableSSLPinning(true);
        HyperSnapSDK.setShouldUseSignature(true);
        HyperSnapSDK.setShouldReturnRawResponse(true);
        HyperSnapSDK.setHttpTimeoutValues(30, 120, 120);
    }

}