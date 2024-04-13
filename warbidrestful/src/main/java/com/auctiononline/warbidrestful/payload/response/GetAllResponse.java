package com.auctiononline.warbidrestful.payload.response;

import com.auctiononline.warbidrestful.models.User;
import com.auctiononline.warbidrestful.payload.dto.Paging;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetAllResponse {
    private int code;
    private String status;
    private String message;
    private Paging paging;
    private List<?> data;

    public GetAllResponse(int code, String status, String message, List<?> data){
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
