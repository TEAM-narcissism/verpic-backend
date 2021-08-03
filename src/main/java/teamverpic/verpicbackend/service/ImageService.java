//package teamverpic.verpicbackend.service;
//
//import org.springframework.stereotype.Service;
//import teamverpic.verpicbackend.domain.Image;
//import teamverpic.verpicbackend.dto.ImageDto;
//import teamverpic.verpicbackend.repository.ImageRepository;
//
//import javax.transaction.Transactional;
//
//@Service
//public class ImageService {
//    private ImageRepository imageRepository;
//
//    public ImageService(ImageRepository imageRepository) {
//        this.imageRepository = imageRepository;
//    }
//
//    @Transactional
//    public Long saveFile(ImageDto imageDto) {
//        return imageRepository.save(imageDto.toEntity()).getId();
//    }
//
//    @Transactional
//    public ImageDto getImage(Long id) {
//        Image image = imageRepository.findById(id).get();
//
//        ImageDto imageDto = ImageDto.builder()
//                .id(id)
//                .origFilename(image.getOrigImgName())
//                .filename(image.getImgName())
//                .filePath(image.getImgPath())
//                .build();
//        return imageDto;
//    }
//}
