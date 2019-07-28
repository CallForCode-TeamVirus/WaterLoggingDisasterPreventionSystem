package com.wlpds.service;

import java.io.File;
/*import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
 */
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*import java.util.Arrays;
import java.util.Map;
 */
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.MediaType;
/*import com.ibm.cloud.sdk.core.security.basicauth.BasicAuthConfig;
import com.ibm.cloud.sdk.core.service.security.IamOptions;
import com.ibm.watson.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.visual_recognition.v3.model.ClassifyOptions;
 */
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


@Service
public class VisuallyRecognisedData {
	Logger logger = LoggerFactory.getLogger(VisuallyRecognisedData.class);

	private String textMessage = Constants.Blank;

	public String getTextMessage() {
		return textMessage;
	}

	/*
	 * Not Working :: Multiple issues with matching.
	 */


	/*
	 * public void getVisuallyRecognisedData() throws IOException {
	 * 
	 * IamOptions options = new IamOptions.Builder() .apiKey(apikey) .build();
	 * 
	 * BasicAuthConfig option2 = new BasicAuthConfig.Builder().username(
	 * "rPR3Hr_BDWdo_UYAg0k3GOYEZU9FMZmjoj1OQycnWqB2").build();
	 * 
	 * VisualRecognition service = new VisualRecognition("2018-03-19", option2);
	 * //service.setApiKey(
	 * "YXBpa2V5OnJQUjNIcl9CRFdkb19VWUFnMGszR09ZRVpVOUZNWm1qb2oxT1F5Y25XcUIy");
	 * 
	 * //InputStream imagesStream = new FileInputStream("./images (2).jpg");
	 * InputStream imagesStream = new
	 * FileInputStream("C:\\\\Sujay Jain\\waterLog.jpg"); if(imagesStream != null)
	 * System.out.println(true);
	 * 
	 * ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
	 * .imagesFile(imagesStream) .imagesFilename("waterLog.jpg") .threshold((float)
	 * 0.2) .owners(Arrays.asList("me")) .build(); ClassifiedImages result =
	 * service.classify(classifyOptions).execute().getResult();
	 * System.out.println(result); }
	 * 
	 */	
	public void getVisuallyRecognisedData_Java(String path) throws Exception{
		OkHttpClient client = new OkHttpClient();
		File file = new File(String.valueOf(path));
		RequestBody formBody = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart(Constants.VISUAL_HEADER_OWNER, Constants.VISUAL_VALUE_OWNER)
				.addFormDataPart(Constants.VISUAL_HEADER_THRESHOLD, Constants.VISUAL_VALUE_THRESHOLD)
				.addFormDataPart(Constants.VISUAL_HEADER_IMAGE, Constants.VISUAL_HEADER_IMAGEFILE, RequestBody.create(MediaType.parse("image/jpg"), file))
				.build();

		Request request = new Request.Builder().url(new URL(Constants.VISUAL_ENDPOINT))
				.addHeader("Authorization", Constants.VISUAL_API_CREDS)
				.post(formBody)
				.build();

		Response response = client.newCall(request).execute(); 
		if (!response.isSuccessful())
			throw new Exception("Unexpected code " + response);
		//System.out.println(response.message());
		logger.debug(response.message());
		JsonObject jsonobj = new JsonParser().parse(response.body().string()).getAsJsonObject();
		//System.out.println(jsonobj.toString());
		logger.debug(jsonobj.toString());
		this.createMessageFromJava(jsonobj);
	}

	private void createMessageFromJava(JsonObject object) {
		JsonArray array_images = object.getAsJsonArray("images");
		JsonArray array_classifiers = array_images.get(0).getAsJsonObject().getAsJsonArray("classifiers");
		JsonArray array_classes = array_classifiers.get(0).getAsJsonObject().getAsJsonArray("classes");
		if (array_classes.size()>0) {
			int waterLog_percentage = (int)(array_classes.get(0).getAsJsonObject().get("score").getAsFloat() * 100);		
			this.textMessage = String.format(Constants.TEXT_MESSAGE, String.valueOf(waterLog_percentage));
			logger.debug(textMessage);
		}else {
			textMessage = Constants.Blank;
		}
	}

} 