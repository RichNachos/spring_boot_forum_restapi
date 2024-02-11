package com.richnachos.forum.controllers.user.http.demoteuserbyid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemoteUserByIdResponse {
    private boolean demoted;
}
