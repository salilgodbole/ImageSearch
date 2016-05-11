package in.nurturetech.imagesearch.imagesearch;

/**
 * Created by salil on 10/12/15.
 */
public interface Callback<T> {

    void onSuccess(T t);

    void onError(NTError error);
}
