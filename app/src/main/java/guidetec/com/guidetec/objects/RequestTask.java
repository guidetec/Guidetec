package guidetec.com.guidetec.objects;

import android.app.Activity;
import android.os.AsyncTask;

import ai.api.AIDataService;
import ai.api.AIServiceContext;
import ai.api.AIServiceException;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import guidetec.com.guidetec.fragments.ChatFragment;

public class RequestTask  extends AsyncTask<AIRequest, Void, AIResponse> {

    ChatFragment activity;
    private AIDataService aiDataService;
    private AIServiceContext customAIServiceContext;

    RequestTask(ChatFragment activity, AIDataService aiDataService, AIServiceContext customAIServiceContext){
        this.activity = activity;
        this.aiDataService = aiDataService;
        this.customAIServiceContext = customAIServiceContext;
    }

    @Override
    protected AIResponse doInBackground(AIRequest... aiRequests) {
        final AIRequest request = aiRequests[0];
        try {
            return aiDataService.request(request, customAIServiceContext);
        } catch (AIServiceException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(AIResponse aiResponse) {
        ((ChatFragment)activity).callback(aiResponse);
    }
}
