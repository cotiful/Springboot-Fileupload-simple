package site.metacoding.fileupload;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserRepository userRepository;

    // 배포할 때 사용할거임!!
    // @Value("${file.path}")
    // private String folder;

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @GetMapping("/main")
    public String main(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("user", users.get(0));
        // List<User> users = userRepository.findById(1).get();
        // model.addAttribute("user", users);

        return "main";
    }

    @PostMapping("/join")
    public String join(JoinDto joinDto) { // 버퍼로 읽는 거 1. json, 2. 있는 그대로 받고 싶을 때

        UUID uuid = UUID.randomUUID();

        String requestFileName = joinDto.getFile().getOriginalFilename();
        System.out.println("전송받은 파일명:" + requestFileName);

        // uuid가 뒤에 있으면 파일의 확장자가 사라지니 앞에 있어야 한다. 충돌 방지를 위해 uuid 사용
        String imgurl = uuid + "-" + requestFileName;

        // 메모리에 있는 파일 데이터를 파일시스템으로 옮겨야 함.
        // 1. 빈 파일 생성 haha.png
        // File file = new File ("d:₩example~")
        // 2. 빈 파일에 스트림 연결
        // 3. for문 돌리면서 바이트로 쓰면 됨. FileWriter 객체
        // 우리는 상대경로 사용할 예정 (배포되도 똑같다)

        try {
            // 1. 폴더가 이미 만들어져 있어야 함.
            // 2. 리눅스 '/' 사용하고, 윈도우 ₩ 사용! -> os관점이라, 라이브러리가 혹시나 알아서 해줄 수도 안해줄 수도 있다.
            // '/' 젤 처음
            Path filePath = Paths.get("src/main/resources/static/upload/" + imgurl);
            // 파일의 이름을 가져와야 함. joinDto,getFile(),getBytes()
            Files.write(filePath, joinDto.getFile().getBytes());

            // DB 에는 사진 경로가 저장됨
            userRepository.save(joinDto.toEntity(imgurl));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "joinComplete"; // ViewResolver로 만들기 위해선 -> Controller 로
    }
}
