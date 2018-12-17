package com.andy.andymvp;

import android.os.Parcel;


import com.andy.andymvp.data.ResponseData;
import com.andy.andymvp.data.RowsData;
import com.andy.andymvp.interfaces.MainView;
import com.andy.andymvp.presenter.MainPresenterImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterImplTest {

    @Mock
    MainView mainView;

    private MainPresenterImpl mainPresenter;

    @Before
    public void setUp() throws Exception {
        mainPresenter = new MainPresenterImpl(mainView);
    }

    @Test
    public void checkIfRowDataArePassedToView() {
        RowsData test1 = mock(RowsData.class);
        RowsData test2 = mock(RowsData.class);

        List<RowsData> rowlist = new ArrayList<RowsData>(2);
        rowlist.add(test1);
        rowlist.add(test2);

        ResponseData responseData = new ResponseData();
        responseData.setTitle("title");
        responseData.setRows(rowlist);

        Parcel parcel = Parcel.obtain();
        responseData.writeToParcel(parcel, responseData.describeContents());
        parcel.setDataPosition(0);

        ResponseData createdFromParcel = ResponseData.CREATOR.createFromParcel(parcel);

        mainPresenter.onSuccess("sucess", createdFromParcel);
        verify(mainView, times(1)).onGetDataSuccess("success", createdFromParcel);
        verify(mainView, times(1)).hideProgress();

    }

    @Test
    public void checkIfViewIsReleasedOnStop() {
        mainPresenter.onDestroy();
        assertNull(mainPresenter.getMainView());
    }

}