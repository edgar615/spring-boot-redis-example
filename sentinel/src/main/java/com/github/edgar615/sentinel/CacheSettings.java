package com.github.edgar615.sentinel;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@ConfigurationProperties(prefix = "cache.config")
public class CacheSettings {

    private List<CacheSettingsModel> defaultConfigs;

    public List<CacheSettingsModel> getDefaultConfigs() {
        return defaultConfigs;
    }

    public void setDefaultConfigs(List<CacheSettingsModel> defaultConfigs) {
        this.defaultConfigs = defaultConfigs;
    }

    public Map<String, CacheSettingsModel> getCacheConfigAsMap() {
        return defaultConfigs.stream().
                collect(Collectors.toMap(CacheSettingsModel::getCacheName, c -> c));
    }

}