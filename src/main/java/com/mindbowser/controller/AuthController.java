package com.mindbowser.controller;

import com.mindbowser.config.TokenProvider;
import com.mindbowser.repository.UserDao;
import com.mindbowser.dto.ApiMsgResponse;
import com.mindbowser.dto.AuthTokenDto;
import com.mindbowser.dto.UserSignInRequestDto;
import com.mindbowser.dto.UserSignUpRequestDto;
import com.mindbowser.entity.User;
import com.mindbowser.exception.BadRequestException;
import com.mindbowser.service.UserService;
import com.mindbowser.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    UserDao userDao;

    @PreAuthorize("permitAll()")
    @Operation(summary = "Manager Sign Up.", description = " Sign Up Manager (Only email & password is mandatory.)", tags = {"Sign Up / Sign In"}, security = @SecurityRequirement(name = "bearerAuth"))
    @RequestMapping(value="/signup", method = RequestMethod.POST)
    public ResponseEntity<ApiMsgResponse> saveUser(@RequestBody @Valid UserSignUpRequestDto userSignUpRequestDto){
        log.info("Enter: saveUser Method Type: GET: Request Arguments: {}", userSignUpRequestDto);
        User user = userDao.findByEmail(userSignUpRequestDto.getEmail());
        if(user != null && user.getEmail().equalsIgnoreCase(userSignUpRequestDto.getEmail().trim())){
            throw new BadRequestException("Email "+userSignUpRequestDto.getEmail()+" is already exist with us.");
        }

        if (userSignUpRequestDto == null) {
            throw new BadRequestException("Please provide all inputes.");
        }
        if (userSignUpRequestDto.getEmail() == null || userSignUpRequestDto.getEmail().isEmpty()) {
            throw new BadRequestException("Email should not be empty.");
        }
        if (userSignUpRequestDto.getPassword() == null || userSignUpRequestDto.getPassword().isEmpty()) {
            throw new BadRequestException("Password should not be empty.");
        }
        User userResponse = userService.save(userSignUpRequestDto);
        log.info("Exit: saveUser Method Type: GET: Response Arguments: {}", userResponse);
        return ResponseEntity.ok(new ApiMsgResponse(HttpStatus.OK.value(), Constants.SUCCESS, userResponse));
    }

    @PreAuthorize("permitAll()")
    @Operation(summary = "Manager Sign In.", description = " Sign In Manager", tags = {"Sign Up / Sign In"}, security = @SecurityRequirement(name = "bearerAuth"))
    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ResponseEntity<?> generateToken(@RequestBody @Valid UserSignInRequestDto userSignInRequestDto) throws AuthenticationException {

        log.info("Enter: generateToken Method Type: GET: Request Arguments: {}", userSignInRequestDto);
        User user = userDao.findByEmail(userSignInRequestDto.getEmail().trim());
        if (userSignInRequestDto.getEmail() == null || userSignInRequestDto.getEmail().isEmpty() && userSignInRequestDto.getPassword() == null || userSignInRequestDto.getPassword().isEmpty()) {
            throw new BadRequestException("Email and Password should not be empty.");
        }
        if (userSignInRequestDto.getEmail() == null || userSignInRequestDto.getEmail().isEmpty()) {
            throw new BadRequestException("Email should not be empty.");
        }
        if (userSignInRequestDto.getPassword() == null || userSignInRequestDto.getPassword().isEmpty()) {
            throw new BadRequestException("Password should not be empty.");
        }
        if(user==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Email "+userSignInRequestDto.getEmail()+" is not exist with us.");
        }
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userSignInRequestDto.getEmail(),
                        userSignInRequestDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        log.info("Exit: saveUser Method Type: GET: Response Arguments: {}", token);
        return ResponseEntity.ok(new ApiMsgResponse(HttpStatus.OK.value(), Constants.SUCCESS, new AuthTokenDto(token)));
    }
}
