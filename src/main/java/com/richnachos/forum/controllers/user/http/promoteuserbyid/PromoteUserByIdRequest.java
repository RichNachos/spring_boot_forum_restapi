package com.richnachos.forum.controllers.user.http.promoteuserbyid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromoteUserByIdRequest {
    private Long id;
}
