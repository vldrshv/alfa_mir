package ru.alfabank.alfamir.alfa_tv.domain.usecase;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.alfa_tv.data.source.repository.show.ShowRepository;
import ru.alfabank.alfamir.alfa_tv.domain.mapper.ShowUriMapper;
import ru.alfabank.alfamir.alfa_tv.presentation.dto.ShowUri;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.utility.network.IpProvider;

public class GetShowUrl extends UseCase<GetShowUrl.RequestValues, GetShowUrl.ResponseValue> {

    private ShowRepository mShowRepository;
    private ShowUriMapper mShowUriMapper;
    private IpProvider mIpManager;


    @Inject
    public GetShowUrl(ShowRepository showRepository,
                      ShowUriMapper showUriMapper,
                      IpProvider ipManager){
        mShowRepository = showRepository;
        mShowUriMapper = showUriMapper;
        mIpManager = ipManager;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        String showId = requestValues.getShowId() + "";
        String password = requestValues.getPassword();

        String phoneIp = "";
        String cellularIp = mIpManager.getMobileIPAddress();
        String wifiIp = mIpManager.getWifiIPAddress();
        if(!cellularIp.equals("0.0.0.0")){
            phoneIp = cellularIp;
        } else if (!wifiIp.equals("0.0.0.0")){
            phoneIp = wifiIp;
        }

        return mShowRepository.getVideoUri(showId, phoneIp, password)
                .map(mShowUriMapper)
                .flatMap(showUri -> Observable.just(new ResponseValue(showUri)));
    }

    public static class RequestValues implements UseCase.RequestValues {
        private int mShowId;
        private String mPassword;

        public RequestValues(int showId,
                             String password){
            mShowId = showId;
            mPassword = password;
        }

        public int getShowId() {
            return mShowId;
        }

        public String getPassword() {
            return mPassword;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {
        private ShowUri mShowUri;
        ResponseValue(ShowUri showUri){
            mShowUri = showUri;
        }

        public ShowUri getShowUri() {
            return mShowUri;
        }
    }
}