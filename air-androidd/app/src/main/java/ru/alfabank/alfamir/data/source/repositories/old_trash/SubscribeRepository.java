package ru.alfabank.alfamir.data.source.repositories.old_trash;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.alfabank.alfamir.data.dto.old_trash.models.SubscriptionModel;
import ru.alfabank.alfamir.data.source.remote.api.WebService;
import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper;
import ru.alfabank.alfamir.utility.static_utilities.RequestFactory;

import static ru.alfabank.alfamir.Constants.REQUEST_TYPE_GET_ACTIVE_SUBSCRIPTIONS;
import static ru.alfabank.alfamir.Constants.REQUEST_TYPE_SUBSCRIBE;
import static ru.alfabank.alfamir.Constants.REQUEST_TYPE_UNSUBSCRIBE;
import static ru.alfabank.alfamir.Constants.SUBSCRIBE_TYPE_FEED;
import static ru.alfabank.alfamir.Constants.SUBSCRIBE_TYPE_POST;

public class SubscribeRepository implements WebService.WebRequestListener {

    private WebService webService;
    private boolean isInitialized;
    private List<SubscriptionModel> subscriptionModels = new ArrayList<>();
    private String currentIdOrUrl;
    private String currentSubType;

    public SubscribeRepository(WebService webService) {
        this.webService = webService;
    }

    public void initialize() {
        if (!isInitialized) {
            webService.request(RequestFactory.INSTANCE.formGetActiveSubscriptions(), this, REQUEST_TYPE_GET_ACTIVE_SUBSCRIPTIONS);
        }
    }

    public void subscribe(int subscribeType, String postId, String postUrl, String threadId, String type) {
        if (postId == null || postId.equals("")) {
            currentIdOrUrl = type + ";#" + postUrl;
            currentSubType = "site";
        } else {
            currentIdOrUrl = postId;
            currentSubType = "post";
        }
        subscribe(subscribeType, postId, postUrl, threadId, type, this);
    }

    public void subscribeNew(int subscribeType, String postId, String postUrl, String threadId, String type, WebService.WebRequestListener callback) {
        if (postId == null || postId.equals("")) {
            currentIdOrUrl = type + ";#" + postUrl;
            currentSubType = "site";
        } else {
            currentIdOrUrl = postId;
            currentSubType = "post";
            subscriptionModels.add(new SubscriptionModel(currentIdOrUrl, currentSubType));
        }
        subscribe(subscribeType, postId, postUrl, threadId, type, callback);
    }

    public void unsubscribeNew(int subscribeType, String postId, String postUrl, String threadId, String type, WebService.WebRequestListener callback) {
        if (postId == null || postId.equals("")) {
            currentIdOrUrl = type + ";#" + postUrl;
            currentSubType = "site";
        } else {
            currentIdOrUrl = postId;
            currentSubType = "post";

            Iterator<SubscriptionModel> iterator = subscriptionModels.iterator();
            SubscriptionModel item;
            while (iterator.hasNext()) {
                item = iterator.next();
                if (item.getSubid().equals(currentIdOrUrl)) {
                    subscriptionModels.remove(item);
                    break;
                }
            }
        }

        unsubscribe(subscribeType, postId, postUrl, threadId, type, callback);
    }

    public void unsubscribe(int subscribeType, String postId, String postUrl, String threadId, String type) {
        if (postId == null || postId.equals("")) {
            currentIdOrUrl = type + ";#" + postUrl;
            currentSubType = "site";
        } else {
            currentIdOrUrl = postId;
            currentSubType = "post";
        }

        unsubscribe(subscribeType, postId, postUrl, threadId, type, this);
    }

    private void subscribe(int subscribeType, String postId, String postUrl, String threadId, String type, WebService.WebRequestListener callback) {
        if (subscribeType == SUBSCRIBE_TYPE_POST) {
            webService.request(RequestFactory.INSTANCE.subscribeOnPost(postId, postUrl, threadId, type), callback, REQUEST_TYPE_SUBSCRIBE);
        } else if (subscribeType == SUBSCRIBE_TYPE_FEED) {
            webService.request(RequestFactory.INSTANCE.subscribeOnFeed(postUrl, type), callback, REQUEST_TYPE_SUBSCRIBE);
        }
    }

    private void unsubscribe(int subscribeType, String postId, String postUrl, String threadId, String type, WebService.WebRequestListener callback) {
        if (subscribeType == SUBSCRIBE_TYPE_POST) {
            webService.request(RequestFactory.INSTANCE.unsubscribeFromPost(postId, postUrl, threadId, type), callback, REQUEST_TYPE_UNSUBSCRIBE);

        } else if (subscribeType == SUBSCRIBE_TYPE_FEED) {
            webService.request(RequestFactory.INSTANCE.unsubscribeFromFeed(postUrl, type), callback, REQUEST_TYPE_UNSUBSCRIBE);
        }
    }

    public boolean checkIfSubscribed(String idOrUrl) {
        if (subscriptionModels != null) {
            for (SubscriptionModel item : subscriptionModels) {
                if (item.getSubid().equals(idOrUrl)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onResponse(JsonWrapper jsonWrapper, int responseType) {
        switch (responseType) {
            case REQUEST_TYPE_GET_ACTIVE_SUBSCRIPTIONS: {
                subscriptionModels = jsonWrapper.getSubscriptions();
                isInitialized = true;
                break;
            }
            case REQUEST_TYPE_SUBSCRIBE: {
                subscriptionModels.add(new SubscriptionModel(currentIdOrUrl, currentSubType));
                break;
            }
            case REQUEST_TYPE_UNSUBSCRIBE: {
                Iterator<SubscriptionModel> iterator = subscriptionModels.iterator();
                SubscriptionModel item;
                while (iterator.hasNext()) {
                    item = iterator.next();
                    if (item.getSubid().equals(currentIdOrUrl)) {
                        subscriptionModels.remove(item);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onFailure(JsonWrapper jsonWrapper, int responseType) {
        System.out.println();
    }

}
