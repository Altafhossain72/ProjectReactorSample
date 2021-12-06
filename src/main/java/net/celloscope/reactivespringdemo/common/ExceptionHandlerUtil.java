package net.celloscope.reactivespringdemo.common;

import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionHandlerUtil extends Exception{
    public HttpStatus code;
    public String message;
}
