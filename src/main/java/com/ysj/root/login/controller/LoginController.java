package com.ysj.root.login.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysj.root.login.dao.UserDAO;
import com.ysj.root.login.dto.KakaoProfile;
import com.ysj.root.login.dto.OAuthToken;
import com.ysj.root.login.dto.UserDTO;

@Controller
public class LoginController {
	
	@Autowired
	UserDAO mapper;
	
	@RequestMapping(value = "loginView")
	public String showLoginView() {
		return "loginView";
	}
	
	
	@GetMapping(value = "auth/kakao/callback")
	public String JoinView(@RequestParam("code") String code , Model model) {
		System.out.println(code);
		
		//httpsURLConnection url ->옛날 방식, 조금 불편
		// REtrofit2(안드로이드에서쓰는 라이브러리) , OkHttp
		RestTemplate rt = new RestTemplate();
		
		// httpHeader 오브젝트 생성
		HttpHeaders headers = new HttpHeaders();
		// 현재 http형식이 key-value 형식임을 알린다
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8"); // 말그대로 헤더에 담는 값
		
		// httpBody 오브젝트 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "a924c282a86092b8472e6c2885aafe4a");
		params.add("redirect_uri", "http://localhost:8080/auth/kakao/callback");
		//방금 받은 코드임
		params.add("code", code);
		
		// body data와 header값을 가지고있는 하나의 httpEntity가 된다
		// body값 header값을 둘다 하나로 만든다
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = 
				new HttpEntity<>(params,headers);
		
		// http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
		ResponseEntity<String> response = rt.exchange(
				"https://kauth.kakao.com/oauth/token",
				HttpMethod.POST,
				kakaoTokenRequest,
				String.class
				);
		System.out.println(response.getBody());
		
		// Gson, Json Simple, ObjectMapper <- ObjectMapper를 사용할 계획
		
		ObjectMapper objectMapper = new ObjectMapper();
		// 여기다가 json 으로 담아낼 예정
		OAuthToken oauthToken = null;
		
		try {
			// getter,setter가 없거나, 변수이름을 정확하게 기입하지않으면 틀린다
			oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// mapper가 잘된것인지 확인
		System.out.println("카카오 엑세스 토큰 : "+oauthToken.getAccess_token());
		
		
		RestTemplate rt2 = new RestTemplate();
		
		// httpHeader 오브젝트 생성
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer "+oauthToken.getAccess_token());
		// 현재 http형식이 key-value 형식임을 알린다
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8"); // 말그대로 헤더에 담는 값
		
		// body data와 header값을 가지고있는 하나의 httpEntity가 된다
		// body값 header값을 둘다 하나로 만든다
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 = 
				new HttpEntity<>(headers2);
		
		// http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
		ResponseEntity<String> response2 = rt2.exchange(
				"https://kapi.kakao.com/v2/user/me",
				HttpMethod.POST,
				kakaoProfileRequest2,
				String.class
				);
		// 회원 정보까지 조회하는게 response2.getbody()
		
		
		ObjectMapper objectMapper2 = new ObjectMapper();
		// 여기다가 json 으로 담아낼 예정
		KakaoProfile kakaoProfile = null;
		
		try {
			// getter,setter가 없거나, 변수이름을 정확하게 기입하지않으면 틀린다
			kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 카카오 프로파일에 모든정보를 매퍼로 담는과정이다
		
		// 정보를 받는것을 확인
		System.out.println( " 카카오 아이디 (번호)"+kakaoProfile.getId());
		System.out.println( " 카카오 이메일 (String)"+kakaoProfile.getKakao_account().getEmail());
		System.out.println(" 카카오 닉네임 (String)"+kakaoProfile.getProperties().getNickname());
		System.out.println(" 카카오 연령대 (String)"+kakaoProfile.getKakao_account().getAge_range());
		
		
		model.addAttribute("email", kakaoProfile.getKakao_account().getEmail());
		model.addAttribute("ageGroup", kakaoProfile.getKakao_account().getAge_range());
		
		
		
		
		
		
		// response.getBody(); // body형식의 데이터 타입으로 편하게 리턴할수 있다 
		return "joinView";
		
	}
	
	@PostMapping(value ="auth/kakao/saveUser" , produces = "application/json;charset=utf-8")
	@ResponseBody
	public String saveUser(@RequestBody Map<String, Object> mapdto) {
		UserDTO userdto = new UserDTO();
		userdto.setUsername((String)mapdto.get("username"));
		userdto.setEmail((String)mapdto.get("email"));
		userdto.setAgeGroup((String)mapdto.get("ageGroup"));
		userdto.setAge((String)mapdto.get("age"));
		userdto.setSchool((String)mapdto.get("school"));
		userdto.setCreateDate(new Date(System.currentTimeMillis()));
		userdto.setRule("user");
		userdto.setPassWord("암호화해서 저장할 예정");
		userdto.setAuth("kakao");
		//저장
		mapper.insertUser(userdto);
		

		return "{\"Result\" : true}";
	}
	
}





