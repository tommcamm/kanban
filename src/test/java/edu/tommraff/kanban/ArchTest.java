package edu.tommraff.kanban;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("edu.tommraff.kanban");

        noClasses()
            .that()
            .resideInAnyPackage("edu.tommraff.kanban.service..")
            .or()
            .resideInAnyPackage("edu.tommraff.kanban.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..edu.tommraff.kanban.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
