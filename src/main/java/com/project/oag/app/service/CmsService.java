package com.project.oag.app.service;

import com.project.oag.app.entity.CmsSetting;
import com.project.oag.app.entity.ContactMessage;
import com.project.oag.app.repository.CmsSettingRepository;
import com.project.oag.app.repository.ContactMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class CmsService {

    private final CmsSettingRepository cmsSettingRepository;
    private final ContactMessageRepository contactMessageRepository;

    public CmsService(CmsSettingRepository cmsSettingRepository,
                      ContactMessageRepository contactMessageRepository) {
        this.cmsSettingRepository = cmsSettingRepository;
        this.contactMessageRepository = contactMessageRepository;
    }

    public Map<String, String> getConfig() {
        Map<String, String> config = new LinkedHashMap<>();
        cmsSettingRepository.findAll().forEach(setting -> config.put(setting.getKey(), setting.getValue()));
        return config;
    }

    @Transactional
    public Map<String, String> updateConfig(Map<String, String> updates) {
        updates.forEach((key, value) -> {
            CmsSetting setting = cmsSettingRepository.findById(key).orElseGet(() -> {
                CmsSetting created = new CmsSetting();
                created.setKey(key);
                return created;
            });
            setting.setValue(value);
            cmsSettingRepository.save(setting);
        });
        return getConfig();
    }

    @Transactional
    public void saveContact(String name, String email, String message) {
        ContactMessage contact = new ContactMessage();
        contact.setName(name);
        contact.setEmail(email);
        contact.setMessage(message);
        contactMessageRepository.save(contact);
    }
}
