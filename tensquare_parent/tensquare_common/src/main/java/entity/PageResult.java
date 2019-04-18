package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Auther: LUOLUO
 * @Date: 2019/4/16
 * @Description: entity
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    private Long total;
    private List<T> rows;
}
