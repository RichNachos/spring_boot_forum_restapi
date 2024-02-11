package com.richnachos.forum.controllers.user.http.demoteuserbyid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemoteUserByIdRequest {
    private Long id;
}
