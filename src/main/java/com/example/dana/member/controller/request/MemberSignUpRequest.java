package com.example.dana.member.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberSignUpRequest {
    @NotBlank(message = "이메일이 비어있습니다.")
    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])+[.][a-zA-Z]{2,3}$", message = "이메일 주소 양식을 확인해주세요")
    private String email;

    @NotBlank(message = "이름이 비어있습니다.")
    private String username;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()]).{8,}$",
            message = "비밀번호는 최소 8글자, 대문자를 포함, 숫자를 포함, 특수문자 1개 이상을 포함해야 합니다.")
    private String password;
}
