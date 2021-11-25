package com.nlf.calendar;

import com.nlf.calendar.util.LunarUtil;
import com.nlf.calendar.util.TaoUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 道历
 *
 * @author 6tail
 */
public class Tao {

  public static final int BIRTH_YEAR = -2697;

  /**
   * 阴历
   */
  private Lunar lunar;

  public Tao(Lunar lunar) {
    this.lunar = lunar;
  }

  public static Tao fromLunar(Lunar lunar) {
    return new Tao(lunar);
  }

  public static Tao fromYmdHms(int year, int month, int day, int hour, int minute, int second) {
    return Tao.fromLunar(Lunar.fromYmdHms(year + BIRTH_YEAR, month, day, hour, minute, second));
  }

  public static Tao fromYmd(int year, int month, int day) {
    return fromYmdHms(year, month, day, 0, 0, 0);
  }

  public Lunar getLunar() {
    return lunar;
  }

  public int getYear() {
    return lunar.getYear() - BIRTH_YEAR;
  }

  public int getMonth() {
    return lunar.getMonth();
  }

  public int getDay() {
    return lunar.getDay();
  }

  public String getYearInChinese() {
    String y = getYear() + "";
    StringBuilder s = new StringBuilder();
    for (int i = 0, j = y.length(); i < j; i++) {
      s.append(LunarUtil.NUMBER[y.charAt(i) - '0']);
    }
    return s.toString();
  }

  public String getMonthInChinese() {
    return lunar.getMonthInChinese();
  }

  public String getDayInChinese() {
    return lunar.getDayInChinese();
  }

  public List<TaoFestival> getFestivals() {
    List<TaoFestival> l = new ArrayList<TaoFestival>();
    List<TaoFestival> fs = TaoUtil.FESTIVAL.get(getMonth() + "-" + getDay());
    if (null != fs) {
      l.addAll(fs);
    }
    String jq = lunar.getJieQi();
    if ("冬至".equals(jq)) {
      l.add(new TaoFestival("元始天尊圣诞"));
    } else if ("夏至".equals(jq)) {
      l.add(new TaoFestival("灵宝天尊圣诞"));
    }
    // 八节日
    String f = TaoUtil.BA_JIE.get(jq);
    if (null != f) {
      l.add(new TaoFestival(f));
    }
    // 八会日
    f = TaoUtil.BA_HUI.get(lunar.getDayInGanZhi());
    if (null != f) {
      l.add(new TaoFestival(f));
    }
    return l;
  }

  private boolean isDayIn(String[] days) {
    String md = getMonth() + "-" + getDay();
    for (String d : days) {
      if (md.equals(d)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 是否三会日
   *
   * @return true/false
   */
  public boolean isDaySanHui() {
    return isDayIn(TaoUtil.SAN_HUI);
  }

  /**
   * 是否三元日
   *
   * @return true/false
   */
  public boolean isDaySanYuan() {
    return isDayIn(TaoUtil.SAN_YUAN);
  }

  /**
   * 是否八节日
   *
   * @return true/false
   */
  public boolean isDayBaJie() {
    return TaoUtil.BA_JIE.containsKey(lunar.getJieQi());
  }

  /**
   * 是否五腊日
   *
   * @return true/false
   */
  public boolean isDayWuLa() {
    return isDayIn(TaoUtil.WU_LA);
  }

  /**
   * 是否八会日
   *
   * @return true/false
   */
  public boolean isDayBaHui() {
    return TaoUtil.BA_HUI.containsKey(lunar.getDayInGanZhi());
  }

  @Override
  public String toString() {
    return String.format("%s年%s月%s", getYearInChinese(), getMonthInChinese(), getDayInChinese());
  }

  public String toFullString() {
    return String.format("道歷%s年，天運%s年，%s月，%s日。%s月%s日，%s時。", getYearInChinese(), lunar.getYearInGanZhi(), lunar.getMonthInGanZhi(), lunar.getDayInGanZhi(), getMonthInChinese(), getDayInChinese(), lunar.getTimeZhi());
  }

}
