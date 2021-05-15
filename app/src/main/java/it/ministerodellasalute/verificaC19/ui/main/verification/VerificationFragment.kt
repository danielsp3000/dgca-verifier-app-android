/*
 *  ---license-start
 *  eu-digital-green-certificates / dgca-verifier-app-android
 *  ---
 *  Copyright (C) 2021 T-Systems International GmbH and all other contributors
 *  ---
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  ---license-end
 *
 *  Created by climent on 5/7/21 4:56 PM
 */

package it.ministerodellasalute.verificaC19.ui.main.verification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import it.ministerodellasalute.verificaC19.*
import it.ministerodellasalute.verificaC19.databinding.FragmentVerificationBinding
import dgca.verifier.app.decoder.model.VerificationResult
import it.ministerodellasalute.verificaC19.model.PersonModel

@ExperimentalUnsignedTypes
@AndroidEntryPoint
class VerificationFragment: Fragment(), View.OnClickListener {

    private val args by navArgs<VerificationFragmentArgs>()
    private val viewModel by viewModels<VerificationViewModel>()

    private var _binding: FragmentVerificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.closeButton.setOnClickListener(this)
        binding.nextQrButton.setOnClickListener(this)

        viewModel.verificationResult.observe(viewLifecycleOwner){
            setCertStatusUI(it)
        }

        viewModel.certificate.observe(viewLifecycleOwner) { certificate ->
            certificate?.let{
                setPersonData(it.person, it.dateOfBirth)
            }
        }

        viewModel.inProgress.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }

        viewModel.init(args.qrCodeText)
    }

    private fun setCertStatusUI(verificationResult: VerificationResult){
        if(verificationResult.isValid()){
            binding.containerPersonDetails.visibility = View.VISIBLE
            binding.checkmark.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_checkmark_filled)
            binding.certificateValid.text = getString(R.string.certificateValid)
            binding.subtitleText.text = getString(R.string.subtitle_text)

        }else{
            binding.containerPersonDetails.visibility = View.GONE
            binding.checkmark.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_misuse)
            binding.certificateValid.text = getString(R.string.certificateNonValid)
            binding.subtitleText.text = getString(R.string.subtitle_text_nonvalid)
        }
    }

    private fun setPersonData(person: PersonModel, dateOfBirth: String){
        binding.nameText.text = person.givenName.plus(" ").plus(person.familyName)
        binding.nameStandardisedText.text = person.standardisedGivenName.plus(" ").plus(person.standardisedFamilyName)
        binding.birthdateText.text = dateOfBirth.parseFromTo(YEAR_MONTH_DAY, FORMATTED_BIRTHDAY_DATE)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.close_button -> requireActivity().finish()
            R.id.next_qr_button -> findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}