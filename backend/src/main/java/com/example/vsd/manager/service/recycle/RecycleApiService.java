package com.example.vsd.manager.service.recycle;

import com.example.vsd.grpc.messages.BaseEntityProto;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Transactional(readOnly = true)
public interface RecycleApiService {

	Collection<BaseEntityProto> apiFindAll();

}
