package com.example.loginandregister;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.loginandregister.databinding.FragmentLoginBinding;
import com.google.android.material.snackbar.Snackbar;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.rootLayout.setOnClickListener(v -> hideKeyboard());

        String fullText = getString(R.string.don_t_have_an_account_sing_up);
        SpannableString spannableString = new SpannableString(fullText);

        // Find the index of "Sign Up"
        int startIndex = fullText.indexOf("Sign Up");
        int endIndex = startIndex + "Sign Up".length();

        // Apply a color to "Sign Up" if found
        if (startIndex >= 0) {
            int primaryColor = requireContext().getColor(R.color.primary);
            spannableString.setSpan(
                    new ForegroundColorSpan(primaryColor),
                    startIndex,
                    endIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }

        binding.tvSignUp.setText(spannableString);
        binding.tvSignUp.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new RegisterFragment())
                    .addToBackStack(null)
                    .commit();
        });

        binding.sivGoogle.setOnClickListener(v -> Snackbar.make(binding.getRoot(), "Google Login Clicked", Snackbar.LENGTH_SHORT).show());
        binding.sivFacebook.setOnClickListener(v -> Snackbar.make(binding.getRoot(), "Facebook Login Clicked", Snackbar.LENGTH_SHORT).show());
        binding.sivGithub.setOnClickListener(v -> Snackbar.make(binding.getRoot(), "GitHub Login Clicked", Snackbar.LENGTH_SHORT).show());

        binding.btnLogin.setOnClickListener(v -> {
            hideKeyboard();
            String message = "";

            if (binding.tilUsername.getEditText() != null) {
                message += "Username: " + binding.tilUsername.getEditText().getText().toString();
            }
            if (binding.tilPass.getEditText() != null) {
                message += "\nPassword: " + binding.tilPass.getEditText().getText().toString();
            }

            Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", v1 -> {

                    }).show();
        });

        addTextInputListener();
    }

    private void hideKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view == null) {
            view = new View(requireContext());
        }

        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void validateButtonSubmit() {
        boolean isUsernameEmpty = binding.tilUsername.getEditText() == null || binding.tilUsername.getEditText().getText().toString().trim().isEmpty();
        boolean isPasswordEmpty = binding.tilPass.getEditText() == null || binding.tilPass.getEditText().getText().toString().trim().isEmpty();

        if (isUsernameEmpty || isPasswordEmpty) {
            binding.btnLogin.setEnabled(false);
        } else {
            binding.btnLogin.setEnabled(true);
        }
    }

    private void addTextInputListener() {
        TextWatcher handleTextChange = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateButtonSubmit();
            }
        };

        if (binding.tilUsername.getEditText() != null) {
            binding.tilUsername.getEditText().addTextChangedListener(handleTextChange);
        }

        if (binding.tilPass.getEditText() != null) {
            binding.tilPass.getEditText().addTextChangedListener(handleTextChange);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}