import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 有事物，会回滚
 *
 * @author kangyonggan
 * @since 2016/11/30
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-core.xml"})
public abstract class AbstractTransactionalServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    protected Logger log = LogManager.getLogger();

}
