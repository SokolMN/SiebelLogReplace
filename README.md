# SiebelLogReplace
Утилита, которая позволяет заменять бинды для логов в Siebel.
Реализовано с графическими элементами: кнопкой, по которой происодит замена биндов, окна для ввода текста из логов с биндами и окна с конечным результатом.

Пример селекта из логов Siebel:

SELECT
  T1.ROW_ID,
  T1.OPTY_TYPE
FROM S_OPTY T1 WHERE ROW_ID = :1

Obmgr blablablabla Bind Variable 1: 1-5HK1M

После прогона в утилите на выходе получится следующий результат:

SELECT
  T1.ROW_ID,
  T1.OPTY_TYPE
FROM S_OPTY T1 WHERE ROW_ID = '1-5HK1M'
