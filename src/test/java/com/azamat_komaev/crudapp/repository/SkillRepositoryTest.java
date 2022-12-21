package com.azamat_komaev.crudapp.repository;

import com.azamat_komaev.crudapp.model.Skill;
import com.azamat_komaev.crudapp.model.Status;
import com.azamat_komaev.crudapp.repository.gson.GsonSkillRepositoryImpl;
import com.azamat_komaev.crudapp.util.RepositoryTestUtil;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

public class SkillRepositoryTest {
    private final SkillRepository repository = new GsonSkillRepositoryImpl();
    private final String FILE_PATH = "src/main/resources/skills.json";

    @Before
    @After
    public void clearFileContent() {
        RepositoryTestUtil.clearFileContent(this.FILE_PATH);
    }

    private void saveThreeSkills() {
        Skill skill1 = new Skill(1, "Soft-Skills");
        Skill skill2 = new Skill(2, "Hard-Skills");
        Skill skill3 = new Skill(3, "No-Code");

        this.repository.save(skill1);
        this.repository.save(skill2);
        this.repository.save(skill3);
    }

    @Test
    public void testAddSkills() {
        saveThreeSkills();

        List<Skill> skillListFromRepository = this.repository.getAll();
        Skill[] skillArrayFromGetById = {
            this.repository.getById(1),
            this.repository.getById(2),
            this.repository.getById(3)
        };

        assertEquals(3, skillListFromRepository.size());
        assertArrayEquals(skillArrayFromGetById, skillListFromRepository.toArray());
    }

    @Test
    public void testGetSkills() {
        saveThreeSkills();

        assertNull(this.repository.getById(-1));
        assertNull(this.repository.getById(0));
        assertNull(this.repository.getById(4));
        
        assertNotNull(this.repository.getById(1));
        assertNotNull(this.repository.getById(2));
        assertNotNull(this.repository.getById(3));
    }

    @Test
    public void testCheckSkillModelFields() {
        saveThreeSkills();

        Skill skill = this.repository.getById(2);

        assertNotNull(skill);
        assertEquals(Integer.valueOf(2), skill.getId());
        assertEquals("Hard-Skills", skill.getName());
        assertEquals(Status.ACTIVE, skill.getStatus());
    }

    @Test
    public void testUpdateSkill() {
        saveThreeSkills();

        Skill skillBeforeUpdate = this.repository.getById(2);
        Skill skillAfterUpdate;
        Skill skillToUpdate = new Skill();

        skillToUpdate.setId(2);
        skillToUpdate.setName("Sports");

        this.repository.update(skillToUpdate);
        skillAfterUpdate = this.repository.getById(2);

        assertNotEquals(skillBeforeUpdate, skillAfterUpdate);
        assertEquals(Integer.valueOf(2), skillAfterUpdate.getId());
        assertEquals("Sports", skillAfterUpdate.getName());
        assertEquals(Status.ACTIVE, skillAfterUpdate.getStatus());
    }

    @Test
    public void testDeleteSkill() {
        saveThreeSkills();

        int skillListCount = this.repository.getAll().size();
        Skill skillBeforeDelete = this.repository.getById(3);
        this.repository.deleteById(3);
        Skill skillAfterDelete = this.repository.getById(3);

        assertEquals(3, skillListCount);
        assertNotEquals(skillBeforeDelete, skillAfterDelete);
        assertEquals(Integer.valueOf(3), skillAfterDelete.getId());
        assertEquals(skillBeforeDelete.getName(), skillAfterDelete.getName());
        assertEquals(Status.DELETED, skillAfterDelete.getStatus());
    }
}
