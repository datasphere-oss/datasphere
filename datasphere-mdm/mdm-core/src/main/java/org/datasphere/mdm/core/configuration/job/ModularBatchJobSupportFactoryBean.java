package com.huahui.datasphere.mdm.core.configuration.job;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.util.Assert;
import com.huahui.datasphere.mdm.system.service.ModuleService;
import com.huahui.datasphere.mdm.system.type.job.ModularBatchJobFraction;
import com.huahui.datasphere.mdm.system.type.module.Module;

import com.huahui.datasphere.mdm.core.type.annotation.JobRef;
import com.huahui.datasphere.mdm.core.type.job.ModularBatchJobSupport;

/**
 * @author theseusyang on Jan 17, 2020
 */
public class ModularBatchJobSupportFactoryBean extends AbstractFactoryBean<ModularBatchJobSupport> {

    private List<ModularBatchJobFraction> sources;

    private String jobName;

    public ModularBatchJobSupportFactoryBean() {
        super();
        setSingleton(false);
    }
    /**
     * @param jobName the jobName to set
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
    /**
     * Set the source classes list.
     */
    public void setClassesList(List<ModularBatchJobFraction> sources) {
        this.sources = CollectionUtils.isEmpty(sources) ? Collections.emptyList() : sources;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Class<ModularBatchJobSupport> getObjectType() {
        return ModularBatchJobSupport.class;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    protected ModularBatchJobSupport createInstance() {
        return new ModularBatchJobSupport(sources.stream()
                .sorted(Comparator.comparingInt(ModularBatchJobFraction::order))
                .collect(Collectors.toList()));
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // Explicitly set by the user.
        if (Objects.nonNull(sources)) {
            return;
        }

        // Try to gather
        Assert.notNull(jobName, "Job name must not be null.");

        ModuleService moduleService = getBeanFactory().getBean(ModuleService.class);
        this.sources = moduleService.getModules().stream()
            .map(Module::getBatchJobFractions)
            .flatMap(Collection::stream)
            .filter(ppc -> {
                JobRef ref = ppc.getClass().getAnnotation(JobRef.class);
                return Objects.nonNull(ref) && StringUtils.equalsIgnoreCase(ref.value(), jobName);
            })
            .collect(Collectors.toList());
    }
}
