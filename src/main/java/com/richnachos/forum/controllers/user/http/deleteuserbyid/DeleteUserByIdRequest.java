package com.richnachos.forum.controllers.user.http.deleteuserbyid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteUserByIdRequest {
    private Long id;
}
