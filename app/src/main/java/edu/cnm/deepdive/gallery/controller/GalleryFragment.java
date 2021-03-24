package edu.cnm.deepdive.gallery.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.gallery.R;
import edu.cnm.deepdive.gallery.adapter.GalleryAdapter;
import edu.cnm.deepdive.gallery.databinding.FragmentGalleryBinding;
import edu.cnm.deepdive.gallery.model.Image;
import edu.cnm.deepdive.gallery.viewmodel.MainViewModel;
import java.util.List;

public class GalleryFragment extends Fragment {

  private static final int PICK_IMAGE_REQUEST = 1023;
  private FragmentGalleryBinding binding;
  private MainViewModel viewModel;
  private GalleryAdapter adapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu_gallery, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    return super.onOptionsItemSelected(item);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentGalleryBinding.inflate(inflater, container, false);
    Context context = getContext();
    int span = (int) Math.floor(context.getResources().getDisplayMetrics().widthPixels
        / context.getResources().getDimension(R.dimen.gallery_item_width));
    adapter = new GalleryAdapter(context);
    binding.galleryView.setAdapter(adapter);
    binding.addImage.setOnClickListener((v) -> pickImage());
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //noinspection ConstantConditions
    viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
    viewModel.getImage().observe(getViewLifecycleOwner(), this::updateGallery);
    viewModel.getImages().observe(getViewLifecycleOwner(), this::updateGallery);
  }

  private void pickImage() {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent, "Choose image to upload"), PICK_IMAGE_REQUEST);
  }

  private void updateGallery(Image image) {
    List<Image> images = adapter.getImages();
    if(image != null && !images.contains(image)) {
      images.add(0, image);
      adapter.notifyItemInserted(0);
    }
  }

  private void updateGallery(List<Image> images) {
    adapter.getImages().clear();
    adapter.getImages().addAll(images);
    adapter.notifyDataSetChanged();
  }
}