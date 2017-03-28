package com.cat;

import lombok.*;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class Info {
    private int id;//索引,自增
    private String job;//职位
    private String company;
    private String link;//链接
    private String salary;//薪资
    private String place;//地点
}
