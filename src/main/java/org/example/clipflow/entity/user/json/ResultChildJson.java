package org.example.clipflow.entity.user.json;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class ResultChildJson implements Serializable {

    String suggestion; // 建议

    ScenesJson scenes; // 场景 scenes 下包含具体的审核类型（场景）结果
}
