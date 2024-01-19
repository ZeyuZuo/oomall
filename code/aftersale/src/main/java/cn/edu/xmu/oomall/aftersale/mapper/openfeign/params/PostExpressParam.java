package cn.edu.xmu.oomall.aftersale.mapper.openfeign.params;

import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.Consignee;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.*;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class PostExpressParam {

    private Consignee sender;
    private Consignee receiver;
    private Long shopLogisticId;

}
