package authentication.model;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor
public class Product implements Serializable {
    private Integer pid;
    @NonNull
    private String title;
    @NonNull
    private String imgUrl;
    @NonNull
    private String description;
    @NonNull
    private Double price;

}
