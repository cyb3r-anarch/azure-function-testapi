package com.function;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    @FunctionName("HttpExample")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Get the uploaded file content from the request body
        byte[] fileContent = request.getBody().map(String::getBytes).orElse(null);

        if (fileContent == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please upload a file.").build();
        }

        // Parse PDF content to text using PDFBox
        String pdfText = parsePdf(fileContent);

        String jsonResponse = "{\"parsedText\": \"" + pdfText + "\"}";

        return request.createResponseBuilder(HttpStatus.OK).body(jsonResponse).build();


        // Process the extracted text and return the results to the frontend
        // String processedResult = processPdfText(pdfText);
        
    }

    private String parsePdf(byte[] pdfContent) {
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfContent))) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String processPdfText(String pdfText) {
        // Your logic for processing the extracted text goes here
        // This could involve identifying questions, answers, etc.
        // You may need to customize this based on the structure of your PDFs
        return "Processed result: " + pdfText;
    }
}

