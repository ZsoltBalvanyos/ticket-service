package com.zsoltbalvanyos.core.testutils;

import org.springframework.stereotype.Component;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Resets the database tables to the original state before each method
 */
@Component
public class DatabaseCleanup extends AbstractTestExecutionListener {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        testContext.getApplicationContext()
            .getAutowireCapableBeanFactory()
            .autowireBean(this);

        var scriptPath = Objects.requireNonNull(getClass().getClassLoader().getResource("setup.sql")).getPath();
        var script = Files.readString(Path.of(scriptPath));
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery(script).executeUpdate();
        entityManager.getTransaction().commit();
    }
}
