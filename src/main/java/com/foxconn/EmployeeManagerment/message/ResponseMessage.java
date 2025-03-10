package com.foxconn.EmployeeManagerment.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * ResponseMessage
 * @author tuandv
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessage<T> extends BaseMessage implements Serializable {
    private T data;

}
