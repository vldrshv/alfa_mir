package ru.alfabank.alfamir.profile.presentation.dto;

import java.util.List;

import ru.alfabank.alfamir.profile.data.dto.SubProfile;

public class Profile {
    private String mName;
    private String mLogin;
    private String mEmail;
    private String mPosition;
    private String mDepartment;
    private String mFullAddress;
    private String mShortAddress;
    private String mWorkSpaceAddress;
    private String mCity;
    private String mBirthday;
    private String mVacation;
    private String mVacationDelegate;
    private String mAboutMe;
    private String mExpertise;
    private String mPicUrl;
    private String mLocalTime;
    private String mFirstName;
    private String mLastName;
    private String mMiddleName;
    private int mIsFavoured;
    private int mIsLiked;
    private int mLikes;
    private int mIsPhoneFormatted;
    private List<String> mWorkPhone;
    private List<String> mMobilePhone;
    private boolean mIsVacationCurrent;
    private SubProfile administrativeManager;
    private SubProfile functionalManager;
    private List<SubProfile> assistants;

    private Profile(String name,
                    String login,
                    String email,
                    String position,
                    String department,
                    String fullAddress,
                    String shortAddress,
                    String workSpaceAddress,
                    String city,
                    String birthday,
                    String vacation,
                    String vacationDeligate,
                    String aboutMe,
                    String expertise,
                    String picUrl,
                    String localTime,
                    String firstName,
                    String lastName,
                    String middleName,
                    int isFavoured,
                    int isLiked,
                    int likes,
                    int isPhoneFormatted,
                    List<String> workPhone,
                    List<String> mobilePhone,
                    boolean isVacationCurrent,
                    SubProfile administrativeManager,
                    SubProfile functionalManager,
                    List<SubProfile> assistants) {
        mName = name;
        mLogin = login;
        mEmail = email;
        mPosition = position;
        mDepartment = department;
        mFullAddress = fullAddress;
        mShortAddress = shortAddress;
        mWorkSpaceAddress = workSpaceAddress;
        mCity = city;
        mBirthday = birthday;
        mVacation = vacation;
        mVacationDelegate = vacationDeligate;
        mAboutMe = aboutMe;
        mExpertise = expertise;
        mPicUrl = picUrl;
        mLocalTime = localTime;
        mIsLiked = isLiked;
        mLikes = likes;
        mIsPhoneFormatted = isPhoneFormatted;
        mFirstName = firstName;
        mLastName = lastName;
        mMiddleName = middleName;
        mIsFavoured = isFavoured;
        mWorkPhone = workPhone;
        mMobilePhone = mobilePhone;
        mIsVacationCurrent = isVacationCurrent;
        this.administrativeManager = administrativeManager;
        this.functionalManager = functionalManager;
        this.assistants = assistants;
    }

    public String getName() {
        return mName;
    }

    public String getLogin() {
        return mLogin;
    }

    public String getEmail() {
        return mEmail;
    }

    public SubProfile getAdministrativeManager() {
        return administrativeManager;
    }

    public SubProfile getFunctionalManager() {
        return functionalManager;
    }

    public String getPosition() {
        return mPosition;
    }

    public String getDepartment() {
        return mDepartment;
    }

    public List<String> getWorkPhone() {
        return mWorkPhone;
    }

    public List<String> getMobilePhone() {
        return mMobilePhone;
    }

    public String getFullAddress() {
        return mFullAddress;
    }

    public String getShortAddress() {
        return mShortAddress;
    }

    public String getWorkSpaceAddress() {
        return mWorkSpaceAddress;
    }

    public String getCity() {
        return mCity;
    }

    public String getBirthday() {
        return mBirthday;
    }

    public String getVacation() {
        return mVacation;
    }

    public String getVacationDelegate() {
        return mVacationDelegate;
    }

    public String getAboutMe() {
        return mAboutMe;
    }

    public String getExpertise() {
        return mExpertise;
    }

    public String getPicUrl() {
        return mPicUrl;
    }

    public String getLocalTime() {
        return mLocalTime;
    }

    public int getIsLiked() {
        return mIsLiked;
    }

    public int getLikes() {
        return mLikes;
    }

    public int getIsPhoneFormatted() {
        return mIsPhoneFormatted;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getMiddleName() {
        return mMiddleName;
    }

    public int isFavoured() {
        return mIsFavoured;
    }

    public void setFavoured(int favouredStatus) {
        mIsFavoured = favouredStatus;
    }

    public boolean isVacationCurrent() {
        return mIsVacationCurrent;
    }

    public void setIsLiked(int isLiked) {
        mIsLiked = isLiked;
    }

    public List<SubProfile> getAssistants() {
        return assistants;
    }

    public void setAssistants(List<SubProfile> assistants) {
        this.assistants = assistants;
    }

    public static class Builder {
        private String mName;
        private String mLogin;
        private String mEmail;
        private String mPosition;
        private String mDepartment;
        private String mFullAddress;
        private String mShortAddress;
        private String mWorkSpaceAddress;
        private String mCity;
        private String mBirthday;
        private String mVacation;
        private String mVacationDelegate;
        private String mAboutMe;
        private String mExpertise;
        private String mPicUrl;
        private String mLocalTime;
        private String mFirstName;
        private String mLastName;
        private String mMiddleName;
        private int mIsFavoured;
        private int mIsLiked; // transform into boolean?
        private int mLikes;
        private int mIsPhoneFormatted;
        private List<String> mWorkPhone;
        private List<String> mMobilePhone;
        private boolean mIsVacationCurrent;
        private SubProfile mAdministrativeManager;
        private SubProfile mFunctionalManager;
        private List<SubProfile> assistants;

        public Builder name(String name) {
            mName = name;
            return this;
        }

        public Builder login(String login) {
            mLogin = login;
            return this;
        }

        public Builder email(String email) {
            mEmail = email;
            return this;
        }

        public Builder administrativeManager(SubProfile administrativeManager) {
            mAdministrativeManager = administrativeManager;
            return this;
        }

        public Builder functionalManager(SubProfile functionalManager) {
            mFunctionalManager = functionalManager;
            return this;
        }

        public Builder position(String position) {
            mPosition = position;
            return this;
        }

        public Builder department(String department) {
            mDepartment = department;
            return this;
        }

        public Builder workPhone(List<String> workPhone) {
            mWorkPhone = workPhone;
            return this;
        }

        public Builder mobilePhone(List<String> mobilePhone) {
            mMobilePhone = mobilePhone;
            return this;
        }

        public Builder fullAddress(String fullAddress) {
            mFullAddress = fullAddress;
            return this;
        }

        public Builder shortAddress(String shortAddress) {
            mShortAddress = shortAddress;
            return this;
        }

        public Builder workSpaceAddress(String workSpaceAddress) {
            mWorkSpaceAddress = workSpaceAddress;
            return this;
        }

        public Builder city(String city) {
            mCity = city;
            return this;
        }

        public Builder birthday(String birthday) {
            mBirthday = birthday;
            return this;
        }

        public Builder isVacationCurrent(boolean isVacationCurrent) {
            mIsVacationCurrent = isVacationCurrent;
            return this;
        }

        public Builder vacationDelegate(String vacationDelegate) {
            mVacationDelegate = vacationDelegate;
            return this;
        }

        public Builder vacation(String vacation) {
            mVacation = vacation;
            return this;
        }

        public Builder aboutMe(String aboutMe) {
            mAboutMe = aboutMe;
            return this;
        }

        public Builder expertise(String expertise) {
            mExpertise = expertise;
            return this;
        }

        public Builder picUrl(String picUrl) {
            mPicUrl = picUrl;
            return this;
        }

        public Builder localTime(String localTime) {
            mLocalTime = localTime;
            return this;
        }

        public Builder isLiked(int isLiked) {
            mIsLiked = isLiked;
            return this;
        }

        public Builder likes(int likes) {
            mLikes = likes;
            return this;
        }

        public Builder isPhoneFormatted(int isPhoneFormatted) {
            mIsPhoneFormatted = isPhoneFormatted;
            return this;
        }

        public Builder firstName(String firstName) {
            mFirstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            mLastName = lastName;
            return this;
        }

        public Builder middleName(String middleName) {
            mMiddleName = middleName;
            return this;
        }

        public Builder isFavoured(int isFavoured) {
            mIsFavoured = isFavoured;
            return this;
        }

        public Builder assistants(List<SubProfile> assistants) {
            this.assistants = assistants;
            return this;
        }

        public Profile build() {
            return new Profile(mName,
                    mLogin,
                    mEmail,
                    mPosition,
                    mDepartment,
                    mFullAddress,
                    mShortAddress,
                    mWorkSpaceAddress,
                    mCity,
                    mBirthday,
                    mVacation,
                    mVacationDelegate,
                    mAboutMe,
                    mExpertise,
                    mPicUrl,
                    mLocalTime,
                    mFirstName,
                    mLastName,
                    mMiddleName,
                    mIsFavoured,
                    mIsLiked,
                    mLikes,
                    mIsPhoneFormatted,
                    mWorkPhone,
                    mMobilePhone,
                    mIsVacationCurrent,
                    mAdministrativeManager,
                    mFunctionalManager,
                    assistants);
        }
    }
}
