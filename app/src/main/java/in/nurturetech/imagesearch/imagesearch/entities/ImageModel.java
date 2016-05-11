package in.nurturetech.imagesearch.imagesearch.entities;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by salil on 10/12/15.
 */
public class ImageModel implements Parcelable {

    private final String title;
    private final String url;
    private final String width;
    private final String height;

    private ImageModel(String title, String url, String width, String height) {
        this.title = title;
        this.url = url;
        this.width = width;
        this.height = height;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }

    private static ImageModel fromJSONObject(JSONObject jsonObject) {
        ImageModel imageModel = null;
        if (jsonObject != null) {
            String title = jsonObject.optString("title");

            JSONObject thumbnailObject = jsonObject.optJSONObject("thumbnail");
            String url = null;
            String width = "0";
            String height = "0";
            if (thumbnailObject != null) {
                url = thumbnailObject.optString("source");
                width = String.valueOf(thumbnailObject.optInt("width"));
                height = String.valueOf(thumbnailObject.optInt("height"));
            }

            imageModel = new ImageModel(title, url, width, height);
        }

        return imageModel;
    }

    public static List<ImageModel> parseResponse(JSONObject jsonObject) {
        List<ImageModel> imageModelList = null;

        if (jsonObject != null) {
            JSONObject queryObject = jsonObject.optJSONObject("query");
            if (queryObject != null) {
                JSONObject pagesObject = queryObject.optJSONObject("pages");
                JSONArray photosArray = pagesObject.names();
                if (photosArray != null) {
                    for (int i = 0; i < photosArray.length(); i++) {
                        if (imageModelList == null) {
                            imageModelList = new ArrayList<>();
                        }

                        String key = photosArray.optString(i);
                        ImageModel imageModel = fromJSONObject(pagesObject.optJSONObject(key));
                        imageModelList.add(imageModel);
                    }
                }
            }
        }

        return imageModelList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.width);
        dest.writeString(this.height);
    }

    protected ImageModel(Parcel in) {
        this.title = in.readString();
        this.url = in.readString();
        this.width = in.readString();
        this.height = in.readString();
    }

    public static final Parcelable.Creator<ImageModel> CREATOR = new Parcelable.Creator<ImageModel>() {
        public ImageModel createFromParcel(Parcel source) {
            return new ImageModel(source);
        }

        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };
}

