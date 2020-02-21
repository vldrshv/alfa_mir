package ru.alfabank.alfamir.data.source.repositories.old_trash;

import java.util.List;

import ru.alfabank.alfamir.data.dto.old_trash.models.ModelTvShow;

/**
 * Created by U_M0WY5 on 24.11.2017.
 */

public class ShowRepository {
    private List<ModelTvShow> showSchedule;

    public List<ModelTvShow> getShowSchedule() {
        return showSchedule;
    }

    public void setShowSchedule(List<ModelTvShow> showSchedule) {
        this.showSchedule = showSchedule;
    }
}
