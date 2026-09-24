package com.example.vsd.manager.service.recycle;

import com.example.vsd.grpc.messages.BaseEntityProto;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Transactional(readOnly = true)
public interface RecycleApiService {

	List<BaseEntityProto> apiFindAll();

	List<BaseEntityProto> findRecycle(String text);
}
