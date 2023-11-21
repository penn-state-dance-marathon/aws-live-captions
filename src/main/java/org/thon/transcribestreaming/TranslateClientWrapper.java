package org.thon.transcribestreaming;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.translate.TranslateClient;
import software.amazon.awssdk.services.translate.model.TranslateTextRequest;
import software.amazon.awssdk.services.translate.model.TranslateTextResponse;
import software.amazon.awssdk.core.exception.SdkException;

public class TranslateClientWrapper {

    private final TranslateClient translateClient;

    /**
     * @return AWS credentials to be used to connect to Translate service. This
     *         example uses the default credentials
     *         provider, which looks for environment variables (AWS_ACCESS_KEY_ID
     *         and AWS_SECRET_ACCESS_KEY) or a credentials
     *         file on the system running this program.
     */
    private static AwsCredentialsProvider getCredentials() {
        return DefaultCredentialsProvider.create();
    }

    public TranslateClientWrapper() {
        // Initialize AWS Translate Client with AWS SDK for Java v2
        this.translateClient = TranslateClient.builder()
                .credentialsProvider(getCredentials())
                .region(Region.US_EAST_2)
                .build();
    }

    public String translate(String text, String sourceLangCode, String targetLangCode) {
        TranslateTextRequest request = TranslateTextRequest.builder()
                .text(text)
                .sourceLanguageCode(sourceLangCode)
                .targetLanguageCode(targetLangCode)
                .build();

        try {
            TranslateTextResponse response = translateClient.translateText(request);
            return response.translatedText();
        } catch (SdkException e) {
            System.err.println(e.getMessage());
            // Implement retry logic or other error handling as needed
            // For example, you could retry the request here, or return a custom error
            // message
            return "Translation failed due to SDK exception.";
        }
    }

    // Make sure to close the client when done
    public void close() {
        translateClient.close();
    }
}
