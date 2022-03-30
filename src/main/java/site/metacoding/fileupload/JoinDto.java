package site.metacoding.fileupload;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JoinDto {
    private String username; // from 태그 name = "username"
    private MultipartFile file; // form 태그 name = "file"

    // imgUrl 은 불러와서 데이터베이스에 넣어줘야 함
    public User toEntity(String imgurl) {
        User user = new User();
        user.setUsername(username);
        user.setImgurl(imgurl);
        return user;
    }
}
