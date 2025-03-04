package org.example.clipflow.entity.user.json;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class ResultJson implements Serializable {

    Integer code; // 状态码

    String message; // 提示信息

    ResultChildJson resultChile; // 结果体 resultChile

}
