package net.madhwang.test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.junit.Test;

/**
 * @author madhwang
 *
 */
public class SlotPayoutTest {

	private final int PAY_TABLE[][] = { { 1, 1, 1, 1, 1 }, { 2, 2, 2, 2, 2 }, { 3, 3, 3, 3, 3 }, { 1, 2, 3, 2, 1 }, { 3, 2, 1, 2, 3 } };
	private final int PAY_TABLE_CNT = PAY_TABLE.length;

	/* 슬롯 개수 */
	private final int SLOT_CNT = 5;

	private final int SLOT_RESULT[][] = { { 5, 0, 6 }, { 4, 5, 0 }, { 4, 5, -1 }, { 5, 0, 6 }, { 4, 5, 0 } };

	/* wild 슬롯의 index 번호 */
	private final int WILD_SLOT_IDX = 0;

	/* wild2x 슬롯의 index 번호 */
	private final int WILD_MULTIPLY_SLOT_IDX = -1;

	/* 당첨된 라인의 비트맵. 1이면 당첨된 라인 */
	private final int PAY_TABLE_WIN_BITMAP[] = { 0, 0, 0, 0, 0 };

	/* 각 라인의 당첨 금액 */
	private final long PAY_TABLE_WIN_AMOUNT[] = { 0, 0, 0, 0, 0 };

	/* 7 연속 체크 가능한 마지막 인덱스 */
	private final int LAST_STRAIGHT7_SLOT_IDX = 5;

	private final HashMap<String, Integer> equalSlotPayAmount = new HashMap<String, Integer>();
	private final HashMap<String, Integer> straight7SlotPayAmount = new HashMap<String, Integer>();

	@Test
	public void testPayTest() {

		this.initSlotPayAmountMap();

		long totalPayAmount = 0;

		final int PAY_TABLE_MID_IDX = PAY_TABLE_CNT / 2;

		/* 수행 시간 테스트 */
		Calendar now = Calendar.getInstance();
		Date startTime = now.getTime();
		System.out.println("startTime = " + startTime.getTime());
		/*//수행 시간 테스트 */

		for (int i = 0, j = PAY_TABLE_CNT - 1; i <= PAY_TABLE_MID_IDX || i < j; i++, j -= i) {

			long frontPayAmount = this.calPayAmount(PAY_TABLE[i]);

			totalPayAmount += frontPayAmount;

			PAY_TABLE_WIN_BITMAP[i] = (frontPayAmount > 0 ? 1 : 0);
			PAY_TABLE_WIN_AMOUNT[i] = frontPayAmount;
			if (i == PAY_TABLE_MID_IDX) {
				break;
			}
			long rearPayAmount = this.calPayAmount(PAY_TABLE[j]);
			totalPayAmount += rearPayAmount;
			PAY_TABLE_WIN_BITMAP[j] = (rearPayAmount > 0 ? 1 : 0);
			PAY_TABLE_WIN_AMOUNT[j] = rearPayAmount;
		}

		/*		for (int i = 0; i < PAY_TABLE_CNT; i++) {
		
					long frontPayAmount = this.calPayAmount(PAY_TABLE[i]);
		
					totalPayAmount += frontPayAmount;
		
					PAY_TABLE_WIN_BITMAP[i] = (frontPayAmount > 0 ? 1 : 0);
					PAY_TABLE_WIN_AMOUNT[i] = frontPayAmount;
		
				}*/

		/* 수행 시간 테스트 */
		Calendar now2 = Calendar.getInstance();
		Date entTime = now2.getTime();
		System.out.println("endTime =" + entTime.getTime() + "-" + startTime.getTime() + "/ 1000 = " + ((entTime.getTime() - startTime.getTime()) / 1000.0));
		/* 수행 시간 테스트 */
		System.out.println("===========================");
		System.out.print("PAY_TABLE_WIN_BITMAP = ");
		for (int i = 0; i < PAY_TABLE_CNT; i++) {
			System.out.print(PAY_TABLE_WIN_BITMAP[i] + " , ");
		}
		System.out.println("");

		System.out.print("PAY_TABLE_WIN_AMOUNT = ");
		for (int i = 0; i < PAY_TABLE_CNT; i++) {
			System.out.print(PAY_TABLE_WIN_AMOUNT[i] + " , ");
		}

		System.out.println("");
		System.out.println("totalPayAmount = " + totalPayAmount);
	}

	/* pay table 의 초기화 */
	private void initSlotPayAmountMap() {
		equalSlotPayAmount.put("3_3", 8);
		equalSlotPayAmount.put("3_4", 25);
		equalSlotPayAmount.put("3_5", 100);

		equalSlotPayAmount.put("4_3", 6);
		equalSlotPayAmount.put("4_4", 20);
		equalSlotPayAmount.put("4_5", 60);

		equalSlotPayAmount.put("5_3", 4);
		equalSlotPayAmount.put("5_4", 12);
		equalSlotPayAmount.put("5_5", 40);

		equalSlotPayAmount.put("6_3", 1);
		equalSlotPayAmount.put("6_4", 3);
		equalSlotPayAmount.put("6_5", 10);

		equalSlotPayAmount.put("0_5", 10000);

		straight7SlotPayAmount.put("3", 2);
		straight7SlotPayAmount.put("4", 7);
		straight7SlotPayAmount.put("5", 25);

	}

	/**
	 * pay 금액 체크
	 * @param payTable //PAY_TABLE
	 * @return
	 */
	private long calPayAmount(int payTable[]) {

		boolean hasWildMuliplySlot = false;

		int wildSlotCount = 0;

		int equalSlotCount = 0;
		int straight7SlotCount = 0;

		int resultSlot[] = { 0, 0, 0, 0, 0 };

		for (int i = 0; i < SLOT_CNT; i++) {

			/*
			 * payTable에 있는 slot idx 에 대한 slotSymbol 값을 가져온다 
			 * payTable[i] 에 있는 slot index 는 0이 아닌 1부터 시작하기 때문에 1을 빼야한다.
			 * 
			 */
			int slotSymbol = SLOT_RESULT[i][payTable[i] - 1];

			if (slotSymbol > WILD_SLOT_IDX) {
				resultSlot[i] = slotSymbol;

				/* 이전에 wildSlotCount 가 있으면 현재 또는 처음의 slotSymbol 로 바꾼다 */
				while (wildSlotCount > 0) {
					/* 
					 * 첫 슬롯의 슬롯 심볼이 0 이면, 현재 슬롯 번호로. 0이 아니면 좌측 기준이므로, 첫 슬롯 심볼로 변경 
					 * 슬롯 pay 기준은 왼쪽 기준이며 따라서 
					 * 첫번째 슬롯의 결과값이 wild 이면 
					 * 처음 일반 슬롯이 나올때 이전의 wild 를 바꿔줘야 하며,
					 * 첫번째 슬롯의 결과값이 wild가 아니면,
					 * 왼쪽 기준으로 바꿔준다.
					 */
					resultSlot[i - wildSlotCount] = (resultSlot[0] == 0 ? slotSymbol : resultSlot[0]);
					wildSlotCount--;
				}

			} else {//if (slotSymbol <= WILD_SLOT_IDX) 

				if (slotSymbol == WILD_MULTIPLY_SLOT_IDX) {
					hasWildMuliplySlot = true;
				}

				/* wild 2x 도 0 으로 넣는다 */
				resultSlot[i] = (resultSlot[0] == 0 ? slotSymbol : resultSlot[0]);

				//resultSlot[0]이면 wildSlotCount 증가 아니면 증가시키지 않음

				if (resultSlot[0] == 0) {
					wildSlotCount++;
				}
			}

			/* 
			 * 일단 세개는 맞아야 하므로 여기서 체크한다 
			 * 
			 * 슬롯 5개 기준 으로 i>2 , i==2 순으로 비교하면 8회, i==2 , i>2 순으로 비교하면 9회를 비교해야 한다.
			 * 5개를 초과하면 i>2가 더 적게 비교한다.
			 * 
			 */
			if (i > 2) {

				/*
				 * equalSlotCount 나 straight7SlotCount 는 현재 for 루프 인덱스인 i와 일치해야 한다.
				 * 그래야 두 값의 연속성이 보장된다.
				 */
				if (equalSlotCount == i && resultSlot[i] == resultSlot[0]) {
					equalSlotCount++;
				}

				if (straight7SlotCount == i && resultSlot[i] <= LAST_STRAIGHT7_SLOT_IDX) {
					straight7SlotCount++;

					/* straight7SlotCount 가 증가하지 않았다면 여기서 끝 */
					if (straight7SlotCount == i) {
						break;
					}
				}

			} else if (i == 2) {
				if (resultSlot[0] == resultSlot[1] && resultSlot[0] == resultSlot[2]) {
					equalSlotCount = 3;
				}

				if (resultSlot[0] <= LAST_STRAIGHT7_SLOT_IDX && resultSlot[1] <= LAST_STRAIGHT7_SLOT_IDX && resultSlot[2] <= LAST_STRAIGHT7_SLOT_IDX) {
					straight7SlotCount = 3;
				}

				/* 최소 세개는 맞아야 하는데 맞지 않으면 0 리턴 */
				if (equalSlotCount < 3 && straight7SlotCount < 3) {
					return 0L;
				}
			}
		}

		long equalSlotAmount = 0L;
		if (equalSlotCount >= 3) {
			equalSlotAmount = this.calEqualSlotAmount(resultSlot[0], equalSlotCount);
		}

		long straight7SoltAmount = 0L;
		if (straight7SlotCount >= 3) {
			straight7SoltAmount = this.calStraight7SlotAmount(straight7SlotCount);
		}

		long payAmount = Math.max(equalSlotAmount, straight7SoltAmount) * (hasWildMuliplySlot == true ? 2 : 1);

		return payAmount;
	}

	private long calEqualSlotAmount(int slotSymbol, int equalSlotCount) {

		String key = slotSymbol + "_" + equalSlotCount;
		return equalSlotPayAmount.get(key);
	}

	private long calStraight7SlotAmount(int straight7SlotCount) {
		return straight7SlotPayAmount.get(String.valueOf(straight7SlotCount));
	}

}
