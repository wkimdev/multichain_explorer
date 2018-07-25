package kr.doublechain.basic.explorer.serviceTest;

import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.JsonObject;

import kr.doublechain.basic.explorer.service.DccService;
import kr.doublechain.basic.explorer.service.UpdateBlockService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdateBlockServiceTest {

	@Autowired
	DccService dccService;

	@Autowired
	UpdateBlockService updateBlockService;

	@Test
	public void checkBlockTest() throws Exception {
		System.out.println(updateBlockService.checkBlock().toString());
	}

//	@Test
	public void mergeBlockTest() throws Exception {
		updateBlockService.mergeBlock(dccService.getBlock(new BigInteger("0")));
	}

//	@Test
	public void mergeTxTest() throws Exception {
		updateBlockService
				.mergeTx(dccService.getTx("29d97b40654161dc4d5c598bda14cb68134490858f3d00ec4c4a015cb755f08d"));
	}

//	@Test
	public void Test() throws Exception {
		for (int i = 0; i < 1000; i++) {
			JsonObject jo = dccService.getBlock(new BigInteger(i + ""));
			updateBlockService.mergeBlock(jo);
			updateBlockService.mergeTx(jo);
		}
	}

//	@Test
	public void removeBlocksTest() throws Exception {
		updateBlockService.removeBlocks(new BigInteger("0"));
	}

//	@Test
//	public void startTest() throws Exception {
//		updateBlockService.start();
//	}
}
