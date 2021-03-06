package draganddrop.studybuddy.ui.panel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Logger;

import draganddrop.studybuddy.commons.core.LogsCenter;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.model.task.UniqueTaskList;
import draganddrop.studybuddy.ui.UiPart;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Calendar. Still under construction.
 * Will cleanup code.
 */
public class CalendarPanel extends UiPart<Region> {
    private static final String FXML = "CalendarPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(CalendarPanel.class);
    private ObservableList<Task> taskList;
    private int calendarYear;
    private Month calendarMonth;
    private LocalDate localDate;
    private StackPane dueSoonListPanelPlaceholder;
    private Label dueSoonPanelTitle;

    @FXML
    private Label month;

    @FXML
    private Label year;

    @FXML
    private GridPane monthBox;

    @FXML
    private Button prev;

    @FXML
    private Button next;

    public CalendarPanel(ObservableList<Task> taskList,
                         StackPane dueSoonListPanelPlaceholder, Label dueSoonPanelTitle) {
        super(FXML);
        this.taskList = taskList;
        Date date = new Date();
        localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        this.dueSoonListPanelPlaceholder = dueSoonListPanelPlaceholder;
        this.dueSoonPanelTitle = dueSoonPanelTitle;
        dueSoonPanelTitle.getStyleClass().add("calender_sub_header");

        //reset data in panels for calendar
        dueSoonPanelTitle.setText("Click on a date to see tasks");
        dueSoonListPanelPlaceholder.getChildren().clear();
        dueSoonListPanelPlaceholder.setBackground(
            new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        //generatecalendar
        generateCalendar(localDate.getYear(), localDate.getMonth());

        taskList.addListener(new ListChangeListener<Task>() {
            @Override
            public void onChanged(Change<? extends Task> t) {
                generateCalendar(calendarYear, calendarMonth);
            }
        });
    }

    /**
     * Generates a calendarPanel with give year and month
     *
     * @param calYear  calendar year
     * @param calMonth calendar month
     */
    public void generateCalendar(int calYear, Month calMonth) {

        monthBox.getChildren().clear();

        month.setText(calMonth.toString());
        year.setText(String.valueOf(calYear));
        calendarMonth = calMonth;
        calendarYear = calYear;

        LocalDate newDate = LocalDate.of(calYear, calMonth, 1);

        logger.info("Calendar: Generating calendar for " + calMonth.toString() + calYear);

        //day of week of first day
        int dayOfWeek = newDate.getDayOfWeek().getValue();

        for (int i = 1, j = 0; i <= newDate.lengthOfMonth(); i++) {
            if (dayOfWeek == 7) {
                dayOfWeek = 0;
                j++;
            }
            LocalDate tempDate = LocalDate.of(newDate.getYear(), newDate.getMonth(), i);
            VBox temp = new VBox();
            Pane p = new Pane();
            p.setBackground(new Background(new BackgroundFill(Color.WHEAT, CornerRadii.EMPTY, Insets.EMPTY)));
            monthBox.add(p, dayOfWeek, j);
            Label tempLbl = new Label(i + "");
            tempLbl.setPadding(new Insets(2, 2, 2, 4));
            temp.getChildren().add(tempLbl);
            int count = 0;
            for (Task task : taskList) {
                LocalDateTime[] ldt = task.getDateTimes();
                LocalDateTime tempTaskDueDate = ldt[0];
                LocalDate taskDueDate = LocalDate.of(tempTaskDueDate.getYear(),
                    tempTaskDueDate.getMonth(), tempTaskDueDate.getDayOfMonth());
                if (taskDueDate.equals(tempDate)) {
                    count++;
                }
            }
            if (count > 0) {
                p.setBackground(new Background(
                    new BackgroundFill(Color.rgb(255, 157, 94), CornerRadii.EMPTY, Insets.EMPTY)));
                Label dayTasksLabel = new Label();
                if (count == 1) {
                    dayTasksLabel.setText(count + " Task");
                } else {
                    dayTasksLabel.setText(count + " Tasks");
                }
                dayTasksLabel.setPadding(new Insets(0, 0, 0, 10));
                temp.getChildren().add(dayTasksLabel);
            }
            if (tempDate.equals(localDate)) {
                p.setBackground(new Background(
                    new BackgroundFill(Color.rgb(118, 156, 206), CornerRadii.EMPTY, Insets.EMPTY)));
            }
            monthBox.add(temp, dayOfWeek, j);
            dayOfWeek++;
        }
    }

    /**
     * Generates calendar from previous month.
     */
    public void onClickPrevious() {
        if (calendarMonth.getValue() == 1) {
            generateCalendar(this.calendarYear - 1, Month.DECEMBER);
        } else {
            generateCalendar(this.calendarYear, this.calendarMonth.minus(1));
        }
    }

    /**
     * Generates calendar for next month.
     */
    public void onClickNext() {
        if (calendarMonth.getValue() == 12) {
            generateCalendar(this.calendarYear + 1, Month.JANUARY);
        } else {
            generateCalendar(this.calendarYear, this.calendarMonth.plus(1));
        }
    }

    /**
     * Home button. Generates calendar for current month.
     */
    public void onClickHome() {
        generateCalendar(localDate.getYear(), localDate.getMonth());
    }

    /**
     * Generates task list when clicked on cell.
     *
     * @param event mouse click event
     */
    public void onClickDate(javafx.scene.input.MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if (clickedNode != monthBox) {
            Node parent = clickedNode.getParent();
            while (parent != monthBox) {
                clickedNode = parent;
                parent = clickedNode.getParent();
            }
            Integer colIndex = GridPane.getColumnIndex(clickedNode);
            Integer rowIndex = GridPane.getRowIndex(clickedNode);
            int firstDayOfWeek = LocalDate.of(calendarYear, calendarMonth, 1)
                .getDayOfWeek().getValue();
            if (firstDayOfWeek == 7) {
                firstDayOfWeek = 0;
                rowIndex--;
            }
            int date = rowIndex * 7 + colIndex - firstDayOfWeek + 1;
            LocalDate clickedDate = LocalDate.of(calendarYear, calendarMonth, date);
            viewTaskByDate(clickedDate);
        }

    }

    /**
     * view task of a selected date
     * @param selectedDate selected date
     */
    public void viewTaskByDate(LocalDate selectedDate) {
        ObservableList<Task> taskByDay = generateTaskList(selectedDate);
        TaskListPanel taskByDayPanel = new TaskListPanel(taskByDay);
        dueSoonPanelTitle.setText("Task for " + selectedDate.toString() + " :");
        dueSoonListPanelPlaceholder.getChildren().clear();
        dueSoonListPanelPlaceholder.getChildren().add(taskByDayPanel.getRoot());

    }

    /**
     * @param date date
     * @return list of task on the date
     */
    public ObservableList<Task> generateTaskList(LocalDate date) {
        UniqueTaskList taskByDay = new UniqueTaskList();
        for (Task task : taskList) {
            LocalDateTime[] ldt = task.getDateTimes();
            LocalDateTime tempTaskDueDate = ldt[0];
            LocalDate taskDueDate = LocalDate.of(tempTaskDueDate.getYear(),
                tempTaskDueDate.getMonth(), tempTaskDueDate.getDayOfMonth());
            if (taskDueDate.equals(date)) {
                taskByDay.add(task);
            }
        }
        return taskByDay.asUnmodifiableObservableList();
    }

}
