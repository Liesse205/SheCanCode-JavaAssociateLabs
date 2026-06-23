package com.shecan.lab23.scenario3_product_catalogue.exercise28_jpa_entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaTestUtil {
    private static EntityManagerFactory factory;

    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (factory == null || !factory.isOpen()) {
            if (factory != null && factory.isOpen()) {
                factory.close();
            }
            factory = Persistence.createEntityManagerFactory("test-pu");
        }
        return factory;
    }

    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public static void close() {
        if (factory != null && factory.isOpen()) {
            factory.close();
            factory = null;
        }
    }
}