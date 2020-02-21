package ru.alfabank.alfamir.messenger.presentation.dto;

public class StatusUpdate {
    public final static int PENDING = 0;
    public final static int SENT = 1;
    public final static int DELIVERED = 2;
    public final static int READ = 3;

    private int mPosition;
    private int mStatus;

    private StatusUpdate(int position,
                         int status){
        mPosition = position;
        mStatus = status;
    }

    public int getPosition() {
        return mPosition;
    }

    public int getStatus() {
        return mStatus;
    }

    public static class Builder {
        private int mPosition;
        private int mStatus;

        public Builder position(int position){
            mPosition = position;
            return this;
        }

        public Builder status(int status){
            mStatus = status;
            return this;
        }

        public StatusUpdate build(){
            return new StatusUpdate(mPosition,
                    mStatus);
        }
    }

}
