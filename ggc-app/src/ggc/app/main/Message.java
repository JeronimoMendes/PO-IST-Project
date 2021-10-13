package ggc.app.main;

/** Messages for interaction. */
interface Message {

  /** @return string showing current date. */
  static String currentDate(int date) {
    return "Data actual: " + date;
  }

  /** @return string prompting for value of date to advance */
  static String advanceDate() {
    return "Dias a avancar: ";
  }

  /**
   * @param available  available balance
   * @param accounting accounting balance
   * @return string describing balance.
   */
  static String currentBalance(double available, double accounting) {
    return "Saldo disponível: " + Math.round(available) + "\n" + "Saldo contabilístico: " + Math.round(accounting);
  }

}
