//package teamverpic.verpicbackend.dto;
//
//import lombok.*;
//import teamverpic.verpicbackend.domain.Image;
//
//@Getter
//@Setter
//@ToString
//@NoArgsConstructor
//public class ImageDto {
//
//    private Long id;
//    private String origFilename;
//    private String filename;
//    private String filePath;
//
//    public Image toEntity() {
//        Image build = Image.builder()
//                .id(id)
//                .origImgName(origFilename)
//                .imgName(filename)
//                .imgPath(filePath)
//                .build();
//        return build;
//    }
//
//    @Builder
//    public ImageDto(Long id, String origFilename, String filename, String filePath) {
//        this.id = id;
//        this.origFilename = origFilename;
//        this.filename = filename;
//        this.filePath = filePath;
//    }
//}
