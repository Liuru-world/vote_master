package liuru.world.vote_master.service.impl;

import liuru.world.vote_master.mapper.LogMapper;
import liuru.world.vote_master.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    public List<HashMap<String,Object>> getLogs() {
        return logMapper.getLogs();
    }
}
