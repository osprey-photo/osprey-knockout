package osprey.competition.knockout;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Test;

public class ModelTest {

//	@Test
	public void testModel() {
		try {
			Model model = new Model();
			model.init();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

	
	@Test
	public void testGetRoundOne() {
		try {
			Model model = new Model();
			model.init();
			
			ArrayList<Combatants> list = model.getNextRoundImages();
			assertTrue(list.size() == 8);
			System.out.println(list);
			
			for (int count=0; count<list.size();count++){
				model.markAsFailed(list.get(count).getImageTwo());
			}
			
			list = model.getNextRoundImages();
			assertTrue(list.size() == 4);
			System.out.println(list);
	
			for (int count=0; count<list.size();count++){
				model.markAsFailed(list.get(count).getImageOne());
			}
	
			list = model.getNextRoundImages();
			assertTrue(list.size() == 2);
			System.out.println(list);
			for (int count=0; count<list.size();count++){
				model.markAsFailed(list.get(count).getImageOne());
			}
			
			list = model.getNextRoundImages();
			assertTrue(list.size() == 1);
			System.out.println(list);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testPower2(){
		
		try {
			Model m = new Model();
			
			assertFalse(m.isPowerOf2(0));
			assertFalse(m.isPowerOf2(1));
			assertFalse(m.isPowerOf2(3));
			assertFalse(m.isPowerOf2(9));
			assertTrue(m.isPowerOf2(2));
			assertTrue(m.isPowerOf2(4));
			assertTrue(m.isPowerOf2(8));
			assertTrue(m.isPowerOf2(16));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
