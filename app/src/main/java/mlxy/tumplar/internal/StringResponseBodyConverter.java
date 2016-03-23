package mlxy.tumplar.internal;

import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import retrofit.Converter;

public class StringResponseBodyConverter implements Converter<ResponseBody, String>{
    @Override
    public String convert(ResponseBody value) throws IOException {
        String s = new String(value.bytes());
        return s;
    }
}
