package cn.idicc.taotie.service.services.data.billund;

import org.springframework.web.multipart.MultipartFile;

public interface DatamartsService {
    String processZipFile(MultipartFile zipFile);
}
